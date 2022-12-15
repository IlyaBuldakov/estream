package ru.develonica.model;

import ru.develonica.model.mapper.SpecializationMapper;

public class SpecializationQueueEntryDataPair {

    private final SpecializationMapper specializationMapper;

    private final QueueEntryData queueEntryData;

    public SpecializationQueueEntryDataPair(SpecializationMapper specializationMapper,
                                            QueueEntryData queueEntryData) {
        this.specializationMapper = specializationMapper;
        this.queueEntryData = queueEntryData;
    }

    public SpecializationMapper getSpecialization() {
        return specializationMapper;
    }

    public QueueEntryData getQueueEntryData() {
        return queueEntryData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SpecializationQueueEntryDataPair that = (SpecializationQueueEntryDataPair) o;

        if (!specializationMapper.equals(that.specializationMapper)) return false;
        return queueEntryData.equals(that.queueEntryData);
    }

    @Override
    public int hashCode() {
        int result = specializationMapper.hashCode();
        result = 31 * result + queueEntryData.hashCode();
        return result;
    }
}
