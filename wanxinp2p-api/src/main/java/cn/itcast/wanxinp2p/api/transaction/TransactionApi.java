package cn.itcast.wanxinp2p.api.transaction;

import cn.itcast.wanxinp2p.api.transaction.model.ProjectDTO;
import cn.itcast.wanxinp2p.api.transaction.model.ProjectQueryDTO;
import cn.itcast.wanxinp2p.common.domain.PageVO;
import cn.itcast.wanxinp2p.common.domain.RestResponse;

/**
 * @Created by maodaiyu on 2021/11/30 上午11:48
 */
public interface TransactionApi {

    /**
     * 借款人发标
     *
     * @return
     */
    RestResponse<ProjectDTO> createProject(ProjectDTO projectDTO);

    /**
     * 检索标的信息
     *
     * @param projectQueryDTO 封装查询条件
     * @param order
     * @param pageNo
     * @param pageSize
     * @param sortBy
     * @return
     */
    RestResponse<PageVO<ProjectDTO>> queryProjects(ProjectQueryDTO projectQueryDTO, String order,
                                                   Integer pageNo, Integer pageSize, String sortBy);

}
