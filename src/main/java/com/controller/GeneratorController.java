package com.controller;


import com.dao.GeneratorMapper;
import com.domain.Column;
import com.utils.GenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/com/generate")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GeneratorController {

    private final GeneratorMapper generatorMapper;


    @GetMapping("/java/{tableName}")
    public void generateCode(@PathVariable String tableName) {
        List<Column> columns = generatorMapper.getColumnsList(tableName);
        GenUtils.generate(tableName, columns);
    }

}
