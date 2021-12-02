package cn.itcast.wanxinp2p.transaction.service;

import cn.itcast.wanxinp2p.api.consumer.model.ConsumerDTO;
import cn.itcast.wanxinp2p.api.transaction.model.ProjectDTO;
import cn.itcast.wanxinp2p.api.transaction.model.ProjectQueryDTO;
import cn.itcast.wanxinp2p.common.domain.CodePrefixCode;
import cn.itcast.wanxinp2p.common.domain.PageVO;
import cn.itcast.wanxinp2p.common.domain.ProjectCode;
import cn.itcast.wanxinp2p.common.domain.RepaymentWayCode;
import cn.itcast.wanxinp2p.common.domain.RestResponse;
import cn.itcast.wanxinp2p.common.domain.StatusCode;
import cn.itcast.wanxinp2p.common.util.CodeNoUtil;
import cn.itcast.wanxinp2p.transaction.agent.ConsumerApiAgent;
import cn.itcast.wanxinp2p.transaction.common.utils.SecurityUtil;
import cn.itcast.wanxinp2p.transaction.entity.Project;
import cn.itcast.wanxinp2p.transaction.mapper.ProjectMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Created by maodaiyu on 2021/11/30 上午11:59
 */
@Slf4j
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService {

    @Autowired
    private ConsumerApiAgent consumerApiAgent;

    @Autowired
    private ConfigService configService;

    @Override
    public ProjectDTO createProject(ProjectDTO projectDTO) {
        RestResponse<ConsumerDTO> consumer = consumerApiAgent.getCurrConsumer(SecurityUtil.getUser().getMobile());

        projectDTO.setUserNo(consumer.getResult().getUserNo());
        projectDTO.setConsumerId(consumer.getResult().getId());
        projectDTO.setProjectNo(CodeNoUtil.getNo(CodePrefixCode.CODE_PROJECT_PREFIX));
        projectDTO.setProjectStatus(ProjectCode.COLLECTING.getCode());
        projectDTO.setStatus(StatusCode.STATUS_OUT.getCode());
        projectDTO.setCreateDate(LocalDateTime.now());
        projectDTO.setRepaymentWay(RepaymentWayCode.FIXED_REPAYMENT.getCode());
        projectDTO.setType("NEW");
        Project project = convert(projectDTO);

        // 设置利率(需要在Apollo上进行配置)
        // 年化利率(借款人视图)
        project.setBorrowerAnnualRate(configService.getBorrowerAnnualRate());
        // 年化利率(投资人视图)
        project.setAnnualRate(configService.getAnnualRate());
        // 年化利率(平台佣金，利差)
        project.setCommissionAnnualRate(configService.getCommissionAnnualRate());
        // 债权转让
        project.setIsAssignment(0);

        // 设置标的名字, 姓名+性别+第N次借款
        String sex = Integer.parseInt(consumer.getResult().getIdNumber().substring(16, 17)) % 2 == 0 ? "女士" : "先生";
        QueryWrapper<Project> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Project::getConsumerId, consumer.getResult().getId());
        project.setName(consumer.getResult().getFullname() + sex + "第" + (count(queryWrapper) + 1) + "次借款");

        // 保存到数据库
        save(project);

        // 设置主键
        projectDTO.setId(project.getId());
        projectDTO.setName(project.getName());

        return projectDTO;
    }

    @Override
    public PageVO<ProjectDTO> queryProjectsByQueryDTO(ProjectQueryDTO projectQueryDTO, String order, Integer pageNo, Integer pageSize, String sortBy) {
        // 1 带条件查询
        QueryWrapper<Project> queryWrapper = new QueryWrapper<>();
        // 标的类型
        if (StringUtils.isNotBlank(projectQueryDTO.getType())) {
            queryWrapper.lambda().eq(Project::getType, projectQueryDTO.getType());
        }
        // 起止年化利率(投资人) -- 区间
        if (null != projectQueryDTO.getStartAnnualRate()) {
            queryWrapper.lambda().ge(Project::getAnnualRate, projectQueryDTO.getStartAnnualRate());
        } if (null != projectQueryDTO.getEndAnnualRate()) {
            queryWrapper.lambda().le(Project::getAnnualRate, projectQueryDTO.getStartAnnualRate());
        }
        // 借款期限 -- 区间
        if (null != projectQueryDTO.getStartPeriod()) {
            queryWrapper.lambda().ge(Project::getPeriod, projectQueryDTO.getStartPeriod());
        } if (null != projectQueryDTO.getEndPeriod()) {
            queryWrapper.lambda().le(Project::getPeriod, projectQueryDTO.getEndPeriod());
        }
        // 标的状态
        if (StringUtils.isNotBlank(projectQueryDTO.getProjectStatus())) {
            queryWrapper.lambda().eq(Project::getProjectStatus, projectQueryDTO.getProjectStatus());
        }

        // 2 分页，构造分页对象
        Page<Project> page = new Page<>(pageNo, pageSize);

        // 3 排序
        if (StringUtils.isNotBlank(order) && StringUtils.isNotBlank(sortBy)) {
            if (order.toLowerCase().equals("asc")) {
                queryWrapper.orderByAsc(sortBy);
            } else if (order.toLowerCase().equals("desc")) {
                queryWrapper.orderByDesc(sortBy);
            }
        } else {
            queryWrapper.lambda().orderByDesc(Project::getCreateDate);
        }

        // 4 执行查询
        IPage<Project> iPage = page(page, queryWrapper);

        // 5 封装结果
        List<ProjectDTO> projectDTOList = convertList(iPage.getRecords());
        return new PageVO<>(projectDTOList, iPage.getTotal(), pageNo, pageSize);
    }

    private Project convert(ProjectDTO projectDTO) {
        if (projectDTO == null) {
            return null;
        }
        Project project = new Project();
        BeanUtils.copyProperties(projectDTO, project);
        return project;
    }

    private List<ProjectDTO> convertList(List<Project> projectList) {
        if (projectList == null) {
            return null;
        }

        List<ProjectDTO> dtoList = new ArrayList<>();
        projectList.forEach(project -> {
            ProjectDTO projectDTO = new ProjectDTO();
            BeanUtils.copyProperties(project, projectDTO);
            dtoList.add(projectDTO);
        });
        return dtoList;
    }
}
