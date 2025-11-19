package com.nixo.ejb.impl;

import com.nixo.entity.News;
import java.util.List;
import jakarta.ejb.Local;

@Local
public interface SharerBeanLocal {

    void addNews(int sharerUserId, String title, String content, String imageUrl);

    void editNews(int newsId, String title, String content, String imageUrl);

    void deleteNews(int newsId);

    List<News> getSharerNews(int sharerUserId);
}
