package fellowship.mealmaestro.models;

import java.time.DayOfWeek;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DateModel {
    private DayOfWeek dayOfWeek;

    public DateModel() {
    }

    public DateModel(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
}
