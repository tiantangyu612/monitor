package monitor.view.domain.entity;

import java.util.Date;

/**
 * Created by bjlizhitao on 2018/1/18.
 * 集群实体
 */
public class Cluster {
    private Integer id;
    /**
     * 集群名称
     */
    private String name;
    /**
     * 所属应用
     */
    private Integer applicationId;
    /**
     * 集群描述
     */
    private String description;
    /**
     * 报警接收人
     */
    private String alarmUser;
    /**
     * 集群报警接收组
     */
    private String alarmGroup;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Integer applicationId) {
        this.applicationId = applicationId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAlarmUser() {
        return alarmUser;
    }

    public void setAlarmUser(String alarmUser) {
        this.alarmUser = alarmUser;
    }

    public String getAlarmGroup() {
        return alarmGroup;
    }

    public void setAlarmGroup(String alarmGroup) {
        this.alarmGroup = alarmGroup;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Cluster{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", applicationId=" + applicationId +
                ", description='" + description + '\'' +
                ", alarmUser='" + alarmUser + '\'' +
                ", alarmGroup='" + alarmGroup + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
