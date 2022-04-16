package cn.edu.ustc.service.impl;

import cn.edu.ustc.service.HospitalSetService;
import cn.edu.ustc.mapper.HospitalSetMapper;
import cn.ustc.yygh.model.hosp.HospitalSet;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HospitalSetServiceImpl extends ServiceImpl<HospitalSetMapper,HospitalSet> implements HospitalSetService {

    @Autowired
    private HospitalSetMapper hospitalSetMapper;

}
