package Operations;

import Model.Image;

import Operations.RGB.RGB;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Brightness.class, name = "BRIGHTNESS"),
        @JsonSubTypes.Type(value = Contrast.class, name = "CONTRAST"),
        @JsonSubTypes.Type(value = Gray.class, name = "GRAY"),
        @JsonSubTypes.Type(value = RGB.class, name = "RGB"),
})

public interface Operation {
    void apply(Image image);
}
