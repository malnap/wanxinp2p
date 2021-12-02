package cn.itcast.wanxinp2p.transaction.service;

import cn.itcast.wanxinp2p.api.transaction.model.ProjectDTO;
import cn.itcast.wanxinp2p.api.transaction.model.ProjectQueryDTO;
import cn.itcast.wanxinp2p.common.domain.PageVO;

/**
 * @Created by maodaiyu on 2021/11/30 上午11:58
 */
public interface ProjectService {

    /**
     * 创建标的
     *
     * @param project
     * @return
     */
    ProjectDTO createProject(ProjectDTO project);

    /**
     * 根据分页条件检索标的信息
     *
     * @param projectQueryDTO
     * @param order
     * @param pageNo
     * @param pageSize
     * @param sortBy
     * @return
     */
    PageVO<ProjectDTO> queryProjectsByQueryDTO(ProjectQueryDTO projectQueryDTO, String order,
                                               Integer pageNo, Integer pageSize, String sortBy);

}
