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

package domain;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionSorterWeightFactory;

public class LectureDifficultyWeightFactory implements SelectionSorterWeightFactory<TimeTablingProblem, LectureOfEduClass> {


    @Override
    public LectureDifficultyWeight createSorterWeight(TimeTablingProblem problem, LectureOfEduClass lecture) {
        return new LectureDifficultyWeight(lecture);
    }

    public static class LectureDifficultyWeight implements Comparable<LectureDifficultyWeight> {

        private final LectureOfEduClass lecture;

        public LectureDifficultyWeight(LectureOfEduClass lecture) {
            this.lecture = lecture;
        }

        @Override
        public int compareTo(LectureDifficultyWeight other) {
            return new CompareToBuilder()
                    .append(lecture.getEduClass(),other.lecture.getEduClass())
                    .append(lecture.getSubject(),other.lecture.getSubject())
                    .append(lecture.getId(), other.lecture.getId())
                    .toComparison();
        }

    }
}
