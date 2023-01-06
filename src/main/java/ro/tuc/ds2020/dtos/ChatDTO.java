package ro.tuc.ds2020.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ChatDTO {
    private String addresser;
    private String receptor;
    private String content;
    private State state;
}
