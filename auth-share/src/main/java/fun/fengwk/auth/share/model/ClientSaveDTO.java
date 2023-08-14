package fun.fengwk.auth.share.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author fengwk
 */
@Data
public class ClientSaveDTO extends BaseClientPropertiesDTO {

    /**
     * 客户端id，首次创建时无需设值
     */
    @NotNull
    private Long id;

}
