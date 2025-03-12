package Operations;

import Model.Image;

import Operations.Histogram.Histogram;
import Operations.RGB.RGB;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Brightness.class, name = "BRIGHTNESS"),
        @JsonSubTypes.Type(value = Contrast.class, name = "CONTRAST"),
        @JsonSubTypes.Type(value = Gray.class, name = "GRAY"),
        @JsonSubTypes.Type(value = RGB.class, name = "RGB"),
        @JsonSubTypes.Type(value = Histogram.class, name = "HISTOGRAM"),
        @JsonSubTypes.Type(value = OpenJFrame.class, name = "OPEN"),
        @JsonSubTypes.Type(value = Save.class, name = "SAVE"),
        @JsonSubTypes.Type(value = GammaNoise.class, name = "GAMMA_NOISE"),
        @JsonSubTypes.Type(value = GaussianNoise.class, name = "GAUSSIAN_NOISE"),
        @JsonSubTypes.Type(value = NegativeExponentialNoise.class, name = "NEGATIVE_EXPONENTIAL_NOISE"),
        @JsonSubTypes.Type(value = RayleighNoise.class, name = "RAYLEIGH_NOISE"),
        @JsonSubTypes.Type(value = SaltPepperNoise.class, name = "SALT_PEPPER_NOISE"),
        @JsonSubTypes.Type(value = UniformNoise.class, name = "UNIFORM_NOISE")
})

public interface Operation {
    void apply(Image image);
}
