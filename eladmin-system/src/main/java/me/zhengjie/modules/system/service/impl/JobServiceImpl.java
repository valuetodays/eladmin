package me.zhengjie.modules.system.service.impl;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.exception.EntityExistException;
import me.zhengjie.modules.system.domain.Job;
import me.zhengjie.modules.system.repository.JobRepository;
import me.zhengjie.modules.system.repository.UserRepository;
import me.zhengjie.modules.system.service.JobService;
import me.zhengjie.modules.system.service.dto.JobDto;
import me.zhengjie.modules.system.service.dto.JobQueryCriteria;
import me.zhengjie.modules.system.service.mapstruct.JobMapper;
import me.zhengjie.utils.CacheKey;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.PageResult;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.RedisUtils;
import me.zhengjie.utils.ValidationUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
* @author Zheng Jie
 * @since 2019-03-29
*/
@ApplicationScoped
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    @Inject
    JobRepository jobRepository;
    @Inject
    JobMapper jobMapper;
    @Inject
    RedisUtils redisUtils;
    @Inject
    UserRepository userRepository;

    @Override
    public PageResult<JobDto> queryAll(JobQueryCriteria criteria, Page pageable) {
//   fixme: 条件查询         Page<Job> page = jobRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        PanacheQuery<Job> paged = jobRepository.findAll().page(pageable);
        List<Job> list = paged.list();
        long count = paged.count();
        return PageUtil.toPage(jobMapper.toDto(list), count);
    }

    @Override
    public List<JobDto> queryAll(JobQueryCriteria criteria) {
        //   fixme: 条件查询       List<Job> list = jobRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder));
        PanacheQuery<Job> paged = jobRepository.findAll();
        List<Job> list = paged.list();
        return jobMapper.toDto(list);
    }

    @Override
    public JobDto findById(Long id) {
        String key = CacheKey.JOB_ID + id;
        Job job = redisUtils.get(key, Job.class);
        if(job == null){
            job = jobRepository.findById(id);
            ValidationUtil.isNull(job.getId(),"Job","id",id);
            redisUtils.set(key, job, 1, TimeUnit.DAYS);
        }
        return jobMapper.toDto(job);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void create(Job resources) {
        Job job = jobRepository.findByName(resources.getName());
        if(job != null){
            throw new EntityExistException(Job.class,"name",resources.getName());
        }
        jobRepository.save(resources);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void update(Job resources) {
        Job job = jobRepository.findById(resources.getId());
        Job old = jobRepository.findByName(resources.getName());
        if(old != null && !old.getId().equals(resources.getId())){
            throw new EntityExistException(Job.class,"name",resources.getName());
        }
        ValidationUtil.isNull( job.getId(),"Job","id",resources.getId());
        resources.setId(job.getId());
        jobRepository.update(resources);
        // 删除缓存
        delCaches(resources.getId());
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void delete(Set<Long> ids) {
        jobRepository.deleteAllByIdIn(ids);
        // 删除缓存
        redisUtils.delByKeys(CacheKey.JOB_ID, ids);
    }

    @Override
    public File download(List<JobDto> jobDtos) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (JobDto jobDTO : jobDtos) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("岗位名称", jobDTO.getName());
            map.put("岗位状态", jobDTO.getEnabled() ? "启用" : "停用");
            map.put("创建日期", jobDTO.getCreateTime());
            list.add(map);
        }
        return FileUtil.downloadExcel(list);
    }

    @Override
    public void verification(Set<Long> ids) {
        if(userRepository.countByJobs(ids) > 0){
            throw new BadRequestException("所选的岗位中存在用户关联，请解除关联再试！");
        }
    }

    /**
     * 删除缓存
     * @param id /
     */
    public void delCaches(Long id){
        redisUtils.del(CacheKey.JOB_ID + id);
    }
}
