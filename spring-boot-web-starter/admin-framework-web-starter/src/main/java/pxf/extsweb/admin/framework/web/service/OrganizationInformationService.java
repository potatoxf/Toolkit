package pxf.extsweb.admin.framework.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import javax.annotation.Nonnull;
import org.springframework.stereotype.Service;
import pxf.extsweb.admin.framework.web.entity.OrganizationInformation;

/**
 * @author potatoxf
 * @date 2021/4/12
 */
@Service
public interface OrganizationInformationService extends IService<OrganizationInformation> {

  /**
   * 构建一个菜单树，这个菜单树并过滤任何权限
   *
   * @return {@code List<OrganizationInformation>}
   */
  @Nonnull
  List<OrganizationInformation> buildOrganizationTree();
  /**
   * 查找员工所属部门
   *
   * @param id 员工ID
   * @return {@code List<OrganizationInformation>}
   */
  @Nonnull
  List<OrganizationInformation> findDepartmentByEmployeeId(Long id);
  /**
   * 查找员工所属角色
   *
   * @param id 员工ID
   * @return {@code List<OrganizationInformation>}
   */
  @Nonnull
  List<OrganizationInformation> findRoleByEmployeeId(Long id);
}
