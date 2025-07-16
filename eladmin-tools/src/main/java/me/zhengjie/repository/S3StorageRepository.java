
package me.zhengjie.repository;

import me.zhengjie.domain.S3Storage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
* @author Zheng Jie
* @date 2025-06-25
*/
public interface S3StorageRepository extends JpaRepository<S3Storage, Long>, JpaSpecificationExecutor<S3Storage> {

	/**
	 * 根据ID查询文件路径
	 * @param id 文件ID
	 * @return 文件路径
	 */
	@Query(value = "SELECT file_path FROM s3_storage WHERE id = ?1", nativeQuery = true)
	String selectFilePathById(Long id);
}