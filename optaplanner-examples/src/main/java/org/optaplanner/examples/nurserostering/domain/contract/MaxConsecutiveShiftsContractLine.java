/*
 * Copyright 2010 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.optaplanner.examples.nurserostering.domain.contract;

import java.util.Objects;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.apache.commons.lang3.StringUtils;

@XStreamAlias("MaxConsecutiveShiftsContractLine")
public class MaxConsecutiveShiftsContractLine extends ContractLine {
    private String shiftTypeId;
    private int maxValue;
    private int weight;

    public String getShiftTypeId() {
        return shiftTypeId;
    }

    public void setShiftTypeId(String shiftTypeId) {
        this.shiftTypeId = shiftTypeId;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public boolean isEnabled() {
        return StringUtils.isNotBlank(shiftTypeId) && maxValue > 0 && weight > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MaxConsecutiveShiftsContractLine that = (MaxConsecutiveShiftsContractLine) o;
        return Objects.equals(getContract(), that.getContract())
            && Objects.equals(getContractLineType(), that.getContractLineType())
            && Objects.equals(getShiftTypeId(), that.getShiftTypeId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getContract(), getContractLineType(), getShiftTypeId());
    }
}
