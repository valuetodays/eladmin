package ${package}.repository;

import ${package}.domain.${className};
import me.vt.MyPanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

/**
* @author ${author}
* @since ${.now?string("yyyy-MM-dd HH:mm")}
**/
@ApplicationScoped
public class ${className}Repository extends MyPanacheRepository<${className}> {

}
