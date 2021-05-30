package pxf.extsweb.admin.framework.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import org.springframework.stereotype.Service;
import pxf.extsweb.admin.framework.web.entity.MenuInformation;

/**
 * @author potatoxf
 * @date 2021/4/12
 */
@Service
public interface MenuInformationService extends IService<MenuInformation> {

  /**
   * 构建一个菜单树，这个菜单树并不过滤任何权限
   *
   * @return {@code List<MenuTree>}
   */
  List<MenuInformation> buildMenuTree();
  /**
   * 构建一个菜单树，这个菜单树并过滤该员工的权限
   *
   * @return {@code List<MenuTree>}
   */
  List<MenuInformation> buildMenuTreeByEmployeeId(Long employeeId);
}
