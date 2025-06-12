package com.gig.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public interface LikeResponseDTO {
    UUID getMemberId();
    String getFirstName();
    String getLastName();
    String getImageUrl();
    LocalDateTime getLikedOn();
}
