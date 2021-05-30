package pxf.extsweb.admin.framework.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import pxf.extsweb.admin.framework.web.entity.EmployeeInformation;

/**
 * @author potatoxf
 * @date 2021/4/12
 */
@Mapper
@Repository
public interface EmployeeInformationMapper extends BaseMapper<EmployeeInformation> {}
