package pxf.extsweb.admin.framework.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;
import pxf.extsweb.admin.framework.web.entity.EmployeeInformation;

/**
 * @author potatoxf
 * @date 2021/4/12
 */
@Service
public interface EmployeeInformationService extends IService<EmployeeInformation> {

  /**
   * 通过用户名找到员工
   *
   * @param username 用户名
   * @return {@code EmployeeInformation}
   */
  EmployeeInformation findEmployeeByUsername(String username);
}
