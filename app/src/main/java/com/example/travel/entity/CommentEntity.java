package com.example.travel.entity;

import java.util.List;


public class CommentEntity {

    public static final int TYPE_COMMENT_PARENT = 1;
    public static final int TYPE_COMMENT_CHILD = 2;
    public static final int TYPE_COMMENT_MORE = 3;
    public static final int TYPE_COMMENT_EMPTY = 4;
    public static final int TYPE_COMMENT_OTHER = 5;

    private List<FirstLevelEntity> firstLevelEntities;
    private long totalCount;

    public List<FirstLevelEntity> getFirstLevelEntities() {
        return firstLevelEntities;
    }

    public void setFirstLevelEntities(List<FirstLevelEntity> firstLevelEntities) {
        this.firstLevelEntities = firstLevelEntities;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"firstLevelBeans\":")
                .append(firstLevelEntities);
        sb.append(",\"totalCount\":")
                .append(totalCount);
        sb.append('}');
        return sb.toString();
    }
}
