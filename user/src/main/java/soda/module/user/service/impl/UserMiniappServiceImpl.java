package soda.module.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import soda.module.user.entity.UserMiniapp;
import soda.module.user.mapper.UserMiniappMapper;
import soda.module.user.service.IUserMiniappService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jobob
 * @since 2019-07-17
 */
@Service
public class UserMiniappServiceImpl extends ServiceImpl<UserMiniappMapper, UserMiniapp> implements IUserMiniappService {

}
