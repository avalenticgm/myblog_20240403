package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.Tag;
import it.cgmconsulting.myblog.exception.ResourceNotFoundException;
import it.cgmconsulting.myblog.repository.TagRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        // In virtù del vincolo di unicità su tag_name, devo verificare che sul db
        // già non esista il tag_name che intendo persistere.
        if(tagRepository.existsByTagName(tagName))
            return null;
        // Se non esiste, istanzio un oggetto Tag che andrò a persistere.
        Tag tag = new  Tag(tagName);
        return tagRepository.save(tag);
    }

    // modificare un Tag esistente
    @Transactional
    public Tag updateTag(String tagName, String newTagName, boolean visible){
        // trovo il tag da modificare
        Tag tag = tagRepository.findByTagName(tagName).orElseThrow(()-> new ResourceNotFoundException("Tag", "tagName", tagName));

            // verifico che non esista un altro record che abbia già come tag name il valore newTagName
            if (tagRepository.existsByTagNameAndIdNot(newTagName, tag.getId()))
                return null;
            // se non esiste procedo alla sovrascrittura del tagName
            tag.setTagName(newTagName);
            tag.setVisible(visible);

        return tag;
    }

    // cercare uno specifico Tag
    public Tag getTag(short id){
        return tagRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Tag", "id", id));
    }
}
