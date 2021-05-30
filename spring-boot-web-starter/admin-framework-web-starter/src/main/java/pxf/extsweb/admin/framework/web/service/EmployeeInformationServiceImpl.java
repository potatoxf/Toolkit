package pxf.extsweb.admin.framework.web.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;
import pxf.extsweb.admin.framework.web.entity.EmployeeInformation;
import pxf.extsweb.admin.framework.web.mapper.EmployeeInformationMapper;

/**
 * @author potatoxf
 * @date 2021/4/12
 */
@Service
public class EmployeeInformationServiceImpl
    extends ServiceImpl<EmployeeInformationMapper, EmployeeInformation>
    implements EmployeeInformationService {

  private final EmployeeInformationMapper employeeInformationMapper;

  public EmployeeInformationServiceImpl(EmployeeInformationMapper employeeInformationMapper) {
    this.employeeInformationMapper = employeeInformationMapper;
  }

  @Override
  public EmployeeInformation findEmployeeByUsername(String username) {
    QueryWrapper<EmployeeInformation> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("username", username);
    List<EmployeeInformation> employeeInformationList =
        employeeInformationMapper.selectList(queryWrapper);
    if (employeeInformationList.isEmpty()) {
      return null;
    }
    Collections.sort(employeeInformationList);
    return employeeInformationList.get(0);
  }
}
