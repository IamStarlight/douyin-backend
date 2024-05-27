package com.bjtu.douyin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 视频表
 * </p>
 *
 * @author Jinxuan Chen
 * @since 2024-05-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("video")
@ApiModel(value="Video对象", description="视频表")
public class Video implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "视频id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "视频标题")
    private String title;

    @ApiModelProperty(value = "视频资源id")
    private String fileId;

    @ApiModelProperty(value = "dian'z")
    private Integer likeCount;

    private LocalDateTime releaseDate;

    private Integer uploaderId;

    private Boolean deleted;


}
