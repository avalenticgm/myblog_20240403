package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.Avatar;
import it.cgmconsulting.myblog.entity.AvatarId;
import it.cgmconsulting.myblog.entity.Post;
import it.cgmconsulting.myblog.entity.User;
import it.cgmconsulting.myblog.payload.response.AvatarResponse;
import it.cgmconsulting.myblog.repository.AvatarRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final AvatarRepository avatarRepository;
    private final PostService postService;

    @Value("${application.image.post.path}")
    private String path;

    public AvatarResponse addAvatar(UserDetails userDetails, long size, int width, int height, String[] extensions, MultipartFile file) throws IOException {
        if (checkSize(file, size) && checkDimensions(width, height, file) && checkExtensions(extensions, file)) {
            Avatar avatar = new Avatar(
                    new AvatarId((User) userDetails),
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes()
            );
            avatarRepository.save(avatar);
            return new AvatarResponse(avatar.getAvatarId().getUserId().getId(), avatar.getFilename(), avatar.getFiletype(), avatar.getData());
        } else return null;
    }

    @Transactional
    public String addPostImage(UserDetails userDetails, long size, int width, int height, String[] extensions, MultipartFile file, int postId) throws IOException {
        Post post = postService.findPostById(postId);
        if(!post.getUserId().equals((User) userDetails))
            return "You can update only your own post";
        if (checkSize(file, size) && checkDimensions(width, height, file) && checkExtensions(extensions, file)) {
            String ext = StringUtils.getFilenameExtension(file.getOriginalFilename());
            String filename = post.getId()+"."+ext;
            post.setImage(filename);

            Path p = Paths.get(path+filename);
            try{
                Files.write(p, file.getBytes());
            } catch (Exception e){
                return null;
            }
        } else
            return null;
        return "New image "+file.getOriginalFilename()+" added to post";
    }


    private boolean checkSize(MultipartFile file, long size) {
        if (file.getSize() > size || file.isEmpty())
            return false;
        return true;
    }

    private BufferedImage fromMultipartfileToBufferedImage(MultipartFile file) {
        BufferedImage bf = null;
        try {
            bf = ImageIO.read(file.getInputStream());
            return bf;
        } catch (IOException e) {
            return null;
        }
    }

    private boolean checkDimensions(int width, int height, MultipartFile file) {
        BufferedImage bf = fromMultipartfileToBufferedImage(file);
        if (bf != null) {
            return (bf.getHeight() <= height && bf.getWidth() <= width);
        }
        return false;
    }

    private boolean checkExtensions(String[] extensions, MultipartFile file) {
        String filename = file.getOriginalFilename(); // ad es. pippo.pluto.png,
        if (filename != null) {
             String ext = filename.substring(filename.lastIndexOf(".") + 1);
             return  Arrays.stream(extensions).anyMatch(ext::equalsIgnoreCase);
        }
            else return false;
        }

    public void removeAvatar(UserDetails userDetails){
        User user = (User) userDetails;
        avatarRepository.deleteById(new AvatarId(user));
    }

}
