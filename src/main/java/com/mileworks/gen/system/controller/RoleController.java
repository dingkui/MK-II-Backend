package com.mileworks.gen.system.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.mileworks.gen.common.annotation.Log;
import com.mileworks.gen.common.domain.QueryRequest;
import com.mileworks.gen.common.exception.MKException;
import com.mileworks.gen.system.domain.Role;
import com.mileworks.gen.system.domain.RoleMenu;
import com.mileworks.gen.system.service.RoleMenuServie;
import com.mileworks.gen.system.service.RoleService;
import com.wuwenze.poi.ExcelKit;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RestController
@RequestMapping("role")
public class RoleController {

	@Autowired
	private RoleService roleService;
	@Autowired
	private RoleMenuServie roleMenuServie;

	private String message;

	@GetMapping
	@RequiresPermissions("role:view")
	public Page<Role> rolePage(QueryRequest request, Role role) {
		EntityWrapper<Role> roleWrapper = new EntityWrapper<>();
		if (StringUtils.isBlank(request.getSortField())) {
			roleWrapper.orderBy("role_id");
		} else {
			roleWrapper.orderBy(request.getSortField());
		}

		if (StringUtils.isNotBlank(role.getRoleName())) {
			roleWrapper.eq("role_name", role.getRoleName());
		}
		if (StringUtils.isNotBlank(role.getCreateTimeFrom()) && StringUtils.isNotBlank(role.getCreateTimeTo())) {
			roleWrapper.and("date_format(CREATE_TIME,'%Y-%m-%d') >= {0}", role.getCreateTimeFrom());
			roleWrapper.and("date_format(CREATE_TIME,'%Y-%m-%d') <= {0}", role.getCreateTimeTo());
		}
		Page<Role> rolePage = new Page<>(request.getPageNum(), request.getPageSize());
		return this.roleService.selectPage(rolePage, roleWrapper);
	}

	@GetMapping("list")
	@RequiresPermissions("role:view")
	public List<Role> roleList(QueryRequest request, Role role) {
		EntityWrapper<Role> roleWrapper = new EntityWrapper<>();
		if (StringUtils.isBlank(request.getSortField())) {
			roleWrapper.orderBy("role_id");
		} else {
			roleWrapper.orderBy(request.getSortField());
		}
		if (StringUtils.isNotBlank(role.getRoleName())) {
			roleWrapper.eq("role_name", role.getRoleName());
		}
		if (StringUtils.isNotBlank(role.getCreateTimeFrom()) && StringUtils.isNotBlank(role.getCreateTimeTo())) {
			roleWrapper.and("date_format(CREATE_TIME,'%Y-%m-%d') >= {0}", role.getCreateTimeFrom());
			roleWrapper.and("date_format(CREATE_TIME,'%Y-%m-%d') <= {0}", role.getCreateTimeTo());
		}
		return this.roleService.selectList(roleWrapper);
	}
	
	@GetMapping("check/{roleName}")
	public boolean checkRoleName(@NotBlank(message = "{required}") @PathVariable String roleName) {
		Role result = this.roleService.findByName(roleName);
		return result == null;
	}

	@GetMapping("menu/{roleId}")
	public List<String> getRoleMenus(@NotBlank(message = "{required}") @PathVariable String roleId) {
		List<RoleMenu> list = this.roleMenuServie.getRoleMenusByRoleId(roleId);
		return list.stream().map(roleMenu -> String.valueOf(roleMenu.getMenuId())).collect(Collectors.toList());
	}

	@Log("新增角色")
	@PostMapping
	@RequiresPermissions("role:add")
	public void addRole(@Valid Role role) throws MKException {
		try {
			this.roleService.createRole(role);
		} catch (Exception e) {
			message = "新增角色失败";
			log.error(message, e);
			throw new MKException(message);
		}
	}

	@Log("删除角色")
	@DeleteMapping("/{roleIds}")
	@RequiresPermissions("role:delete")
	public void deleteRoles(@NotBlank(message = "{required}") @PathVariable String roleIds) throws MKException {
		try {
			String[] ids = roleIds.split(",");
			this.roleService.deleteRoles(ids);
		} catch (Exception e) {
			message = "删除角色失败";
			log.error(message, e);
			throw new MKException(message);
		}
	}

	@Log("修改角色")
	@PutMapping
	@RequiresPermissions("role:update")
	public void updateRole(Role role) throws MKException {
		try {
			this.roleService.updateRole(role);
		} catch (Exception e) {
			message = "修改角色失败";
			log.error(message, e);
			throw new MKException(message);
		}
	}

	@PostMapping("excel")
	@RequiresPermissions("role:export")
	public void export(Role role, QueryRequest request, HttpServletResponse response) throws MKException {
		try {
			EntityWrapper<Role> roleWrapper = new EntityWrapper<>();
			if (StringUtils.isBlank(request.getSortField())) {
				roleWrapper.orderBy("role_id");
			} else {
				roleWrapper.orderBy(request.getSortField());
			}

			if (StringUtils.isNotBlank(role.getRoleName())) {
				roleWrapper.eq("role_name", role.getRoleName());
			}
			if (StringUtils.isNotBlank(role.getCreateTimeFrom()) && StringUtils.isNotBlank(role.getCreateTimeTo())) {
				roleWrapper.and("date_format(CREATE_TIME,'%Y-%m-%d') >= {0}", role.getCreateTimeFrom());
				roleWrapper.and("date_format(CREATE_TIME,'%Y-%m-%d') <= {0}", role.getCreateTimeTo());
			}
			List<Role> roles = this.roleService.selectList(roleWrapper);
			ExcelKit.$Export(Role.class, response).downXlsx(roles, false);
		} catch (Exception e) {
			message = "导出Excel失败";
			log.error(message, e);
			throw new MKException(message);
		}
	}
}
