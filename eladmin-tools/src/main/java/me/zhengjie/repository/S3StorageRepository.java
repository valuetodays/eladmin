package me.zhengjie.repository;

import jakarta.enterprise.context.ApplicationScoped;
import me.zhengjie.MyPanacheRepository;
import me.zhengjie.domain.S3Storage;

import java.util.Objects;

/**
* @author Zheng Jie
 * @since 2025-06-25
*/
@ApplicationScoped
public class S3StorageRepository extends MyPanacheRepository<S3Storage> {

	/**
	 * 根据ID查询文件路径
	 * @param id 文件ID
	 * @return 文件路径
	 */
//	@Query(value = "SELECT file_path FROM s3_storage WHERE id = ?1", nativeQuery = true)
    public String selectFilePathById(Long id) {
        S3Storage db = findById(id);
        if (Objects.isNull(db)) {
            return null;
        }
        return db.getFilePath();
    }
}