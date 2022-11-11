package com.heima.model.search.dtos;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName UserSearchDto.java
 * @Author xazhao
 * @Create 2022.08.27
 * @UpdateUser
 * @UpdateDate 2022.08.27
 * @Description
 * @Version 1.0.0
 */

@Data
public class SearchDto {

    /**
     * 搜索关键字
     */
    private String searchWords;

    /**
     * 当前页
     */
    private int pageNum;

    /**
     * 分页条数
     */
    private int pageSize;

    /**
     * 最小时间,如果是基于上拉分页；
     * 如果是初次搜索传递一个超大时间
     * 如果是上划，传递的是底部文章发布时间
     */
    private Date minBehotTime;

    public int getFromIndex(){
        if(this.pageNum<1)return 0;
        if(this.pageSize<1) this.pageSize = 10;
        return this.pageSize * (pageNum-1);
    }
}
