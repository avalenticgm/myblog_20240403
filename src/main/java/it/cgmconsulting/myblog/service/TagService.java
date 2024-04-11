package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.Tag;
import it.cgmconsulting.myblog.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TagService {

    private final TagRepository tagRepository;  // Dependency injection by constructor. Il costruttore è generato dall'annotazione di Lombok @RequiredArgsConstructor

    // lista di Tag parametrizzata
    public List<Tag> getAllTags(char visible){
        List<Tag> tags = new ArrayList<>();
        if(visible == 'A')
            tags = tagRepository.findAllByOrderByTagName();
        else if (visible == 'Y')
            tags = tagRepository.findByVisibleTrueOrderByTagName();
        else if (visible == 'N')
            tags = tagRepository.findByVisibleFalseOrderByTagName();

        log.info("Tag list contains "+tags.size()+" elements.");
        return tags;
    }

    public Tag createTag(String tagName){
        Tag tag = new  Tag(tagName);
        return tagRepository.save(tag);
    }


    // modificare un Tag esistente

    // cercare uno specifico Tag
}
