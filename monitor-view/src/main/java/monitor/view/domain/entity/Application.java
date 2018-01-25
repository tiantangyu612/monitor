package monitor.view.domain.entity;

import java.util.Date;

/**
 * Created by bjlizhitao on 2018/1/18.
 * 应用实体
 */
public class Application {
    private Integer id;
    /**
     * 应用名称
     */
    private String name;
    /**
     * 所属产品id
     */
    private Integer productId;
    /**
     * 应用描述
     */
    private String description;
    /**
     * 应用负责人
     */
    private String owner;
    /**
     * 创建时间
     */
    private Date createTime = new Date(System.currentTimeMillis());

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Application{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", productId=" + productId +
                ", description='" + description + '\'' +
                ", owner='" + owner + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
