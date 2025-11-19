package com.nixo.ejb.impl;

import com.nixo.entity.News;
import java.util.List;
import jakarta.ejb.Local;

@Local
public interface ReceiverBeanLocal {

    List<News> getAllNews();

    List<News> searchNews(String keyword);

    boolean toggleLike(int userId, int newsId);

    int getUserLikeCountOnNews(int userId, int newsId);
}
