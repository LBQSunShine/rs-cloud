package com.lbq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lbq.mapper.ArticleFileMapper;
import com.lbq.pojo.ArticleFile;
import com.lbq.service.ArticleFileService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文章文件
 *
 * @author lbq
 * @since 2024-04-14
 */
@Service
public class ArticleFileServiceImpl extends ServiceImpl<ArticleFileMapper, ArticleFile> implements ArticleFileService {

    @Override
    public List<ArticleFile> listByArticleId(Integer articleId) {
        LambdaQueryWrapper<ArticleFile> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(ArticleFile::getArticleId, articleId)
                .orderByDesc(ArticleFile::getSeq);
        return super.list(queryWrapper);
    }

    @Override
    public List<String> getArticleFiles() {
        List<ArticleFile> articleFiles = super.list();
        return articleFiles.stream().map(ArticleFile::getUrl).collect(Collectors.toList());
    }

    @Override
    public void saveByArticle(Integer articleId, List<ArticleFile> articleFiles) {
        for (ArticleFile articleFile: articleFiles) {
            articleFile.setArticleId(articleId);
        }
        super.saveBatch(articleFiles);
    }

    @Override
    public void removeByArticleIds(Collection<Integer> articleIds) {
        if (CollectionUtils.isEmpty(articleIds)) {
            return;
        }
        LambdaQueryWrapper<ArticleFile> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ArticleFile::getArticleId, articleIds);
        super.remove(queryWrapper);
    }
}
