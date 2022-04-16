package cn.edu.ustc.controller;

import cn.edu.ustc.service.HospitalSetService;
import cn.ustc.edu.yygh.result.Result;
import cn.ustc.edu.yygh.util.MD5;
import cn.ustc.yygh.model.hosp.HospitalSet;
import cn.ustc.yygh.vo.hosp.HospitalSetQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@Api(tags = "医院设置管理")
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
public class HospitalSetController {

    @Autowired
    private HospitalSetService hospitalSetService;

    //1 查询医院设置表所有信息
    @ApiOperation(value = "获取所有医院设置")
    @GetMapping("findAll")
    public Result findAllHospitalSet() {
        //调用service的方法
        List<HospitalSet> hospitalSets = hospitalSetService.list();
        return Result.ok(hospitalSets);
    }

    //2 逻辑删除医院设置
    @ApiOperation(value = "逻辑删除医院设置")
    @DeleteMapping("{id}")
    public Result removeHospSet(@PathVariable Long id) {
        boolean b = hospitalSetService.removeById(id);
        return b ? Result.ok() : Result.fail();
    }

    //3 条件查询带分页
    @PostMapping("findPageHospSet/{current}/{limit}")
    public Result findPageHospSet(@PathVariable long current,
                                  @PathVariable long limit,
                                  @RequestBody
                                          (required = false) HospitalSetQueryVo hospitalSetQueryVo) {
        Page<HospitalSet> page = new Page<>(current,limit);
        QueryWrapper<HospitalSet> queryWrapper = new QueryWrapper<>();
        if(hospitalSetQueryVo!=null){
            String hosname = hospitalSetQueryVo.getHosname();
            String hoscode = hospitalSetQueryVo.getHoscode();
            if (!StringUtils.isEmpty(hosname))  queryWrapper.like("hosname", hosname);
            if (!StringUtils.isEmpty(hoscode))  queryWrapper.like("hoscode", hoscode);
        }
        Page<HospitalSet> hospitalSetPage = hospitalSetService.page(page, queryWrapper);
        return Result.ok(hospitalSetPage);
    }

    //4 添加医院设置
    @PostMapping("saveHospitalSet")
    public Result saveHospitalSet(@RequestBody HospitalSet hospitalSet) {
        //设置状态：1使用 0不能使用
        hospitalSet.setStatus(1);
        //签名秘钥
        Random random = new Random();
        hospitalSet.setSignKey(MD5.encrypt(System.currentTimeMillis()+ "" + random.nextInt(1000)));
        boolean b = hospitalSetService.save(hospitalSet);
        return b ? Result.ok() : Result.fail();
    }

    //5 根据id获取医院设置
    @GetMapping("getHospSet/{id}")
    public Result getHospSet(@PathVariable Long id) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        return Result.ok(hospitalSet);
    }

    //6 修改医院设置
    @PostMapping("updateHospitalSet")
    public Result updateHospitalSet(@RequestBody HospitalSet hospitalSet) {
        boolean b = hospitalSetService.updateById(hospitalSet);
        return b ? Result.ok() : Result.fail();
    }

    //7 批量删除医院设置
    @DeleteMapping("batchRemove")
    public Result batchRemoveHospitalSet(@RequestBody List<Long> idList) {
        boolean b = hospitalSetService.removeByIds(idList);
        return b ? Result.ok() : Result.fail();
    }

    //8 医院设置锁定和解锁
    @PutMapping("lockHospitalSet/{id}/{status}")
    public Result lockHospitalSet(@PathVariable Long id,
                                  @PathVariable Integer status) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        hospitalSet.setStatus(status);
        boolean b = hospitalSetService.updateById(hospitalSet);
        return b ? Result.ok() : Result.fail();
    }

    //9 发送签名秘钥
    @PutMapping("sendKey/{id}")
    public Result lockHospitalSet(@PathVariable Long id) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        String signKey = hospitalSet.getSignKey();
        String hoscode = hospitalSet.getHoscode();
        //TODO 发送短信
        return Result.ok();
    }


}


