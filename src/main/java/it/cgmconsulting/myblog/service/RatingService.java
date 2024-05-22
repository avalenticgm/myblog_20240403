package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.Post;
import it.cgmconsulting.myblog.entity.Rating;
import it.cgmconsulting.myblog.entity.RatingId;
import it.cgmconsulting.myblog.entity.User;
import it.cgmconsulting.myblog.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final PostService postService;


    public byte addRate(UserDetails userDetails, int postId, byte rate) {
        User user = (User) userDetails;
        Post post = postService.findPostById(postId);
        Rating r = new Rating(new RatingId(user, post), rate);
        ratingRepository.save(r);
        return r.getRate();
    }

    public void deleteRate(UserDetails userDetails, int postId) {
        User user = (User) userDetails;
        // cancellazione secca: non controllo se esiste il record, cancello e basta
        ratingRepository.deleteRating(postId, user.getId());
    }

    public byte getMyRate(UserDetails userDetails, int postId) {
        User user = (User) userDetails;
        Byte rate = ratingRepository.getMyRate(postId, user.getId());
        return rate == null ? 0 : rate;
    }

}
