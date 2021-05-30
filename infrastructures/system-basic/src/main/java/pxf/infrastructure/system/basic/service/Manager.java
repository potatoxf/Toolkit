package pxf.infrastructure.system.basic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.Objects;
import pxf.infrastructure.system.basic.dbentity.Entity;

/**
 * @author potatoxf
 * @date 2021/5/28
 */
public abstract class Manager<E extends Entity, S extends IService<E>> {

  private final S service;

  protected Manager(S service) {
    this.service = Objects.requireNonNull(service, "The service instance must be no null");
  }

  public final S getService() {
    return service;
  }
}
