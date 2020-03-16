package org.firefly.provider.unit.test.runner.theory;

import org.junit.experimental.theories.ParameterSignature;
import org.junit.experimental.theories.ParameterSupplier;
import org.junit.experimental.theories.ParametersSuppliedBy;
import org.junit.experimental.theories.PotentialAssignment;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

@Retention(RetentionPolicy.RUNTIME)
@ParametersSuppliedBy(NumberRange.NumberRangeSupplier.class)
public @interface NumberRange {
    int start() default 0;

    int end();

    class NumberRangeSupplier extends ParameterSupplier {
        @Override
        public List<PotentialAssignment> getValueSources(ParameterSignature sig) {
            NumberRange numberRange = sig.getAnnotation(NumberRange.class);
            int start = numberRange.start();
            int end = numberRange.end();

            List<PotentialAssignment> list = new ArrayList<>();
            for (int i = start; i <= end; i++) {
                list.add(PotentialAssignment.forValue("name", i));
            }
            return list;
        }
    }
}
