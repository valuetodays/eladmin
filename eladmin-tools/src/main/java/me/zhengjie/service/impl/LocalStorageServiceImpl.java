package me.zhengjie.service.impl;

import cn.vt.exception.NotFinishException;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import me.zhengjie.config.properties.FileProperties;
import me.zhengjie.domain.LocalStorage;
import me.zhengjie.repository.LocalStorageRepository;
import me.zhengjie.service.LocalStorageService;
import me.zhengjie.service.dto.LocalStorageDto;
import me.zhengjie.service.dto.LocalStorageQueryCriteria;
import me.zhengjie.service.mapstruct.LocalStorageMapper;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.PageResult;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.ValidationUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
* @author Zheng Jie
 * @since 2019-09-05
*/
@ApplicationScoped
public class LocalStorageServiceImpl implements LocalStorageService {

    @Inject
    LocalStorageRepository localStorageRepository;
    @Inject
    LocalStorageMapper localStorageMapper;
    @Inject
    FileProperties properties;

    @Override
    public PageResult<LocalStorageDto> queryAll(LocalStorageQueryCriteria criteria, Page pageable) {
        // fixme:    Page<LocalStorage> page = localStorageRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        PanacheQuery<LocalStorage> paged = localStorageRepository.findAll().page(pageable);
        List<LocalStorageDto> list = localStorageMapper.toDto(paged.list());
        return PageUtil.toPage(list, paged.count());
    }

    @Override
    public List<LocalStorageDto> queryAll(LocalStorageQueryCriteria criteria){
        // fixme:        return localStorageMapper.toDto(localStorageRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
        List<LocalStorage> pagedRaw = localStorageRepository.findAll().list();
        List<LocalStorageDto> list = localStorageMapper.toDto(pagedRaw);
        return list;
    }

    @Override
    public LocalStorageDto findById(Long id){
        LocalStorage localStorage = localStorageRepository.findById(id);
        ValidationUtil.isNull(localStorage.getId(),"LocalStorage","id",id);
        return localStorageMapper.toDto(localStorage);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public LocalStorage create(String name, File multipartFile) {
        throw new NotFinishException();
        /* fixme
        FileUtil.checkSize(properties.getMaxSize(), multipartFile.length());
        String suffix = FileUtil.getExtensionName(multipartFile.getOriginalFilename());
        String type = FileUtil.getFileType(suffix);
        File file = FileUtil.upload(multipartFile, properties.getPath().getPath() + type +  File.separator);
        if(ObjectUtil.isNull(file)){
            throw new BadRequestException("上传失败");
        }
        try {
            name = StringUtils.isBlank(name) ? FileUtil.getFileNameNoEx(multipartFile.getOriginalFilename()) : name;
            LocalStorage localStorage = new LocalStorage(
                    file.getName(),
                    name,
                    suffix,
                    file.getPath(),
                    type,
                    FileUtil.getSize(multipartFile.getSize())
            );
            return localStorageRepository.save(localStorage);
        }catch (Exception e){
            FileUtil.del(file);
            throw e;
        }*/
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void update(LocalStorage resources) {
        LocalStorage localStorage = localStorageRepository.findById(resources.getId());
        ValidationUtil.isNull( localStorage.getId(),"LocalStorage","id",resources.getId());
        localStorage.copy(resources);
        localStorageRepository.save(localStorage);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            LocalStorage storage = localStorageRepository.findById(id);
            FileUtil.del(storage.getPath());
            localStorageRepository.delete(storage);
        }
    }

    @Override
    public File download(List<LocalStorageDto> queryAll) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (LocalStorageDto localStorageDTO : queryAll) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("文件名", localStorageDTO.getRealName());
            map.put("备注名", localStorageDTO.getName());
            map.put("文件类型", localStorageDTO.getType());
            map.put("文件大小", localStorageDTO.getSize());
            map.put("创建者", localStorageDTO.getCreateBy());
            map.put("创建日期", localStorageDTO.getCreateTime());
            list.add(map);
        }
        return FileUtil.downloadExcel(list);
    }
}
