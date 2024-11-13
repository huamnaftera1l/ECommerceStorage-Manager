package com.jd.dto;

import com.sun.istack.NotNull;

public class ContainerDTO {

    private Integer id;
    private String name;
    private Integer capacity;
    private Integer availableCapacity;

    public ContainerDTO() {
    }

    public ContainerDTO(String name, Integer capacity) {
        this.name = name;
        this.capacity = capacity;
        this.availableCapacity = capacity; // 初始可用容量等于总容量
    }


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

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getAvailableCapacity() {
        return availableCapacity;
    }

    public void setAvailableCapacity(Integer availableCapacity) {
        this.availableCapacity = availableCapacity;
    }

    // toString 方法用于调试
    @Override
    public String toString() {
        return "ContainerDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", capacity=" + capacity +
                ", availableCapacity=" + availableCapacity +
                '}';
    }
}