package ru.develonica.model;

import java.util.Objects;
import java.util.UUID;
import ru.develonica.model.mapper.SpecializationMapper;

public class QueueEntryData {

    SpecializationMapper specialization;

    UUID userUUID;

    String userQueueCode;

    public QueueEntryData(SpecializationMapper specialization, UUID userUUID) {
        this.specialization = specialization;
        this.userUUID = userUUID;
    }

    public SpecializationMapper getSpecialization() {
        return specialization;
    }

    public UUID getUserUUID() {
        return userUUID;
    }

    public String getUserQueueCode() {
        return userQueueCode;
    }

    public void setUserQueueCode(String userQueueCode) {
        this.userQueueCode = userQueueCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QueueEntryData that = (QueueEntryData) o;

        if (!Objects.equals(specialization, that.specialization))
            return false;
        if (!Objects.equals(userUUID, that.userUUID)) return false;
        return Objects.equals(userQueueCode, that.userQueueCode);
    }

    @Override
    public int hashCode() {
        int result = specialization != null ? specialization.hashCode() : 0;
        result = 31 * result + (userUUID != null ? userUUID.hashCode() : 0);
        result = 31 * result + (userQueueCode != null ? userQueueCode.hashCode() : 0);
        return result;
    }
}
