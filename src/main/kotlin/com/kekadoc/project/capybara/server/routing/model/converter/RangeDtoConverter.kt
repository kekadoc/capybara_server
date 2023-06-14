package com.kekadoc.project.capybara.server.routing.model.converter

import com.kekadoc.project.capybara.server.common.converter.Converter
import com.kekadoc.project.capybara.server.domain.model.common.Range
import com.kekadoc.project.capybara.server.routing.model.RangeDto

object RangeDtoConverter : Converter.Bidirectional<RangeDto, Range> {

    override fun convert(value: RangeDto): Range = Range(
        from = value.from,
        count = value.count,
        query = value.query,
    )

    override fun revert(value: Range): RangeDto = RangeDto(
        from = value.from,
        count = value.count,
        query = value.query,
    )

}