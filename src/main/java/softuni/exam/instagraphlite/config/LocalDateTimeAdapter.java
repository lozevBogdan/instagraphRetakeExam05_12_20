package softuni.exam.instagraphlite.config;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {

    @Override
    public LocalDateTime unmarshal(String date) throws Exception {
        return LocalDateTime.parse(date,
                DateTimeFormatter.ofPattern("yyyy-MM-dd' 'HH:mm:ss"));
    }

    @Override
    public String marshal(LocalDateTime localDate) throws Exception {
        return localDate.toString();
    }
}
