package com.chess.mybatis.mapper.custom;

/**
 * @author 周润斌
 * Date: 2018/9/5
 * Time: 11:49
 * Description:
 */
public interface AccountCustomMapper {
    /**
     * 获取最大编号
     * @return
     */
    Integer selectMaxId();

}
