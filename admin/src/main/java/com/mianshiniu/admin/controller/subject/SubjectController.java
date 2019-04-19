package com.mianshiniu.admin.controller.subject;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mianshiniu.manager.subject.entity.SubjectEntity;
import com.mianshiniu.manager.subject.mapper.SubjectMapper;
import com.mianshiniu.manager.subject.service.impl.SubjectServiceImpl;
import com.mianshiniu.manager.system.entity.UserEntity;
import com.mianshiniu.manager.system.service.UserService;
import com.redimybase.framework.bean.R;
import com.redimybase.framework.model.datamodel.table.TableModel;
import com.redimybase.framework.web.BaseController;
import com.redimybase.security.utils.SecurityUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 面试题接口
 * Created by Vim 2019/2/19 14:25
 *
 * @author Vim
 */
@RestController
@RequestMapping("app/subject")
public class SubjectController extends BaseController<String, SubjectEntity, SubjectMapper, SubjectServiceImpl> {

    @Override
    public Object query(HttpServletRequest request) {

        return null;
    }

    @PostMapping("list")
    public R<?> list(HttpServletRequest request) {
        String msg = null;
        TableModel<SubjectEntity> model = new TableModel<>();
        Page<SubjectEntity> page = (Page<SubjectEntity>) buildPageRequest(request);
        if (page == null) {
            page = new Page<>(1, 8);
        }

        //是否为搜索
        String search = request.getParameter("search");

        String currentUserId = SecurityUtil.getCurrentUserId();
        Wrapper wrapper;
        if (StringUtils.isBlank(currentUserId)) {
            //如果当前登录用户为空则为未登录用户
            wrapper = buildPageWrapper(buildWrapper(new String[]{"AND_status_EQ"}, new String[]{String.valueOf(SubjectEntity.Status.开启)}), getQueryKey(request), getQuerySearch(request));
        } else {
            wrapper = buildPageWrapper(buildWrapper(getQueryColumn(request), getQueryValue(request)), getQueryKey(request), getQuerySearch(request));
        }

        model.setData(getService().page(page, wrapper));

        if ("1".equals(search) && model.getData() != null && ((ArrayList) model.getData()).size() > 0) {
            //搜索一次扣除积分
            //TODO 这里虽然做了返回失败,但还是进行了查询数据
            if (currentUserId == null){
                return R.fail("请登录后再搜索");
            }

            UserEntity user = userService.getOne(new QueryWrapper<UserEntity>().lambda().eq(UserEntity::getId, currentUserId).select(UserEntity::getId, UserEntity::getCoin));
            if (user.getCoin() == null) {
                user.setCoin(0D);
            }
            if (user.getCoin() <= 0) {
                return R.fail("牛逼值不足,无法进行搜索,请提交面试题补充");
            }
            if (user.getCoin() <= 10) {
                msg = String.format("牛逼值已不足10点,当前牛逼值:%s 点,请及时提交面试题补充", user.getCoin());
            }
            user.setCoin(user.getCoin() - 1);
            userService.updateById(user);
        }
        if (StringUtils.isEmpty(msg)) {
            return new R<>(model);
        } else {
            return R.custom(R.成功, msg, model);
        }
    }

    @Override
    public void beforeSave(SubjectEntity entity) {
        if (StringUtils.isBlank(entity.getId())) {
            //新增
            String currentUserId = SecurityUtil.getCurrentUserId();
            entity.setStatus(SubjectEntity.Status.审核中);
            entity.setCreateTime(new Date());
            entity.setUserId(currentUserId);
            //提交一次增加积分
            /*UserEntity user = userService.getOne(new QueryWrapper<UserEntity>().lambda().eq(UserEntity::getId, currentUserId).select(UserEntity::getId, UserEntity::getCoin));
            if (user.getCoin() == null) {
                user.setCoin(0D);
            }
            user.setCoin(user.getCoin() + 5);
            userService.updateById(user);*/
        }
    }

    @Override
    public R<?> save(SubjectEntity entity) {
        super.save(entity);
        return R.ok("提交成功,请耐心等待审核...");
    }

    @Autowired
    private UserService userService;

    @Autowired
    private SubjectServiceImpl service;

    @Override
    protected SubjectServiceImpl getService() {
        return service;
    }
}
