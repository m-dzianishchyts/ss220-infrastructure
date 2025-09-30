package club.ss220.port.redis.mapper;

import club.ss220.core.shared.IngamePost;
import club.ss220.port.redis.presentation.RedisIngamePost;
import jakarta.annotation.Nullable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

@Mapper(componentModel = "spring")
public interface IngamePostMapper {

    @Mapping(target = "serverHost", source = "serverAddress", qualifiedByName = "mapServerHost")
    @Mapping(target = "serverPort", source = "serverAddress", qualifiedByName = "mapServerPort")
    @Mapping(target = "image", source = "imageBase64", qualifiedByName = "mapImage")
    IngamePost toIngamePost(RedisIngamePost presentation) throws IOException;

    @Named("mapServerHost")
    default String mapServerHost(String serverAddress) {
        return serverAddress.split(":")[0];
    }

    @Named("mapServerPort")
    default Integer mapServerPort(String serverAddress) {
        return Integer.parseInt(serverAddress.split(":")[1]);
    }

    @Nullable
    @Named("mapImage")
    default BufferedImage mapImage(String imageBase64) throws IOException {
        if (imageBase64 == null || imageBase64.isBlank()) {
            return null;
        }
        byte[] bytes = Base64.getDecoder().decode(imageBase64);
        return ImageIO.read(new ByteArrayInputStream(bytes));
    }
}
