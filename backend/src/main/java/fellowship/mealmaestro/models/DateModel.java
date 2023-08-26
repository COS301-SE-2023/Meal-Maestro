package fellowship.mealmaestro.models;

import java.time.DayOfWeek;
import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DateModel {
    private LocalDate date;

    public DateModel() {
    }

    public DateModel(LocalDate date) {
        this.date = date;
    }

    public DayOfWeek getDayOfWeek() {
        return date.getDayOfWeek();
    }
}
