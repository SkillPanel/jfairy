package com.devskiller.jfairy.producer.person.locale.it;

import com.devskiller.jfairy.data.DataMaster;
import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.DateProducer;
import com.devskiller.jfairy.producer.person.NationalIdentificationNumberFactory;
import com.devskiller.jfairy.producer.person.NationalIdentificationNumberProperties;

/**
 * Creates providers of Italian tax codes (Codice Fiscale).
 *
 * @since 0.10.0
 */
public class ItNationalIdentificationNumberFactory implements NationalIdentificationNumberFactory {

    private final DataMaster   dataMaster;
    private final BaseProducer baseProducer;
    private final DateProducer dateProducer;

    public ItNationalIdentificationNumberFactory(DataMaster dataMaster, BaseProducer baseProducer, DateProducer dateProducer) {
        this.dataMaster = dataMaster;
        this.baseProducer = baseProducer;
        this.dateProducer = dateProducer;
    }

    /**
     * Creates a provider configured with the given properties.
     *
     * @param properties desired number features, e.g. date of birth, sex and name
     * @return a new tax code provider
     */
    @Override
    public ItNationalIdentificationNumberProvider produceNationalIdentificationNumberProvider(NationalIdentificationNumberProperties.Property... properties) {
        return new ItNationalIdentificationNumberProvider(dataMaster, dateProducer, baseProducer, properties);
    }
}
