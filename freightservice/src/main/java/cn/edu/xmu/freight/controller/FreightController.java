package cn.edu.xmu.freight.controller;

import cn.edu.xmu.freight.model.bo.FreightModelBo;
import cn.edu.xmu.freight.model.vo.FreightModelInfoVo;
import cn.edu.xmu.freight.model.vo.FreightModelRetVo;
import cn.edu.xmu.freight.model.vo.FreightModelVo;
import cn.edu.xmu.freight.model.vo.WeightModelInfoVo;
import cn.edu.xmu.freight.service.FreightService;
import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.annotation.Depart;
import cn.edu.xmu.ooad.annotation.LoginUser;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;

/**
 * @author issyu 30320182200070
 * @date 2020/12/5 1:24
 */
@Api(value = "运费服务",tags ="freight")
@RestController
@RequestMapping(value = "/freight",produces = "application/json;charset=UTF-8")
public class FreightController {


    private static final Logger logger = LoggerFactory.getLogger(FreightController.class);

    @Autowired
    private FreightService freightService;

    @Autowired
    private HttpServletResponse httpServletResponse;

    /**
     * 获取店铺中商品的运费模板
     * @author issyu 30320182200070
     * @date 2020/12/5 1:33
     */

    @ApiOperation(value = "获取店铺中商品的运费模板",produces="application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",dataType = "String",name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path",dataType = "int",name = "Id", value = "店铺Id", required = true),
            @ApiImplicitParam(paramType = "query",dataType = "String",name = "name", value = "模板名称", required = false),
            @ApiImplicitParam(paramType = "query",dataType = "int",name = "page", value = "页码", required = false),
            @ApiImplicitParam(paramType = "query",dataType = "int",name = "pageSize", value = "每页数目", required = false),
    })
    @ApiResponses({
            @ApiResponse(code = 0,message = "成功")
    })
    @Audit
    @GetMapping("/shops/{id}/freightmodels")
    public Object getFreightModelsInShop(
            @PathVariable("id") Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false,defaultValue = "1") Integer page,
            @RequestParam(required = false,defaultValue = "1") Integer pageSize){

        FreightModelVo freightModelVo = new FreightModelVo(id,name);
        ReturnObject<PageInfo<VoObject>> returnObject = freightService.getFreghtModelsInShop(freightModelVo,page,pageSize);
        return Common.getPageRetObject(returnObject);
    }

    /**
     * 定义店铺中商品的运费模板
     * @author issyu 30320182200070
     * @date 2020/12/5 1:33
     */

    @ApiOperation(value = "定义店铺中商品的运费模板",produces="application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",dataType = "String",name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path",dataType = "int",name = "Id", value = "店铺Id", required = true),
            @ApiImplicitParam(paramType = "body",dataType = "FreightModelVo",name = "freightModelVo", value = "运费模板资料", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 0,message = "成功")
    })
    @Audit
    @PostMapping("/shops/{id}/freightmodels")
    public Object defineFreightModel(
            @Depart @ApiIgnore Long departId,
            @PathVariable("id") Long id,
            @RequestBody(required = false) FreightModelVo freightModelVo , BindingResult bindingResult){
        Object returnObject = Common.processFieldErrors(bindingResult,httpServletResponse);
        if (null != returnObject) {
            logger.debug("validate fail");
            return returnObject;
        }

        return Common.getRetObject(freightService.defineFreightModel(freightModelVo,id,departId));
    }


    /**
     * 店家或管理员为店铺定义默认运费模板
     * @author 王薪蕾
     * @date 2020/12/7
     */

    @ApiOperation(value = "店家或管理员为店铺定义默认运费模板",produces="application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",dataType = "String",name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path",dataType = "int",name = "shopId", value = "店铺Id", required = true),
            @ApiImplicitParam(paramType = "path",dataType = "int",name = "Id", value = "运费模板Id", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,message = "成功")
    })
    @Audit
    @PostMapping("/shops/{shopId}/freight_models/{id}/default")
    public Object postFreightModelToShop(
            @LoginUser Long userId,
            @PathVariable("shopId") Long shopId,
            @PathVariable("id") Long id){
        return Common.getRetObject(freightService.postFreightModelToShop(userId,shopId,id));
    }
    /**
     * 管理员定义重量模板明细
     * @author 王薪蕾
     * @date 2020/12/8
     */

    @ApiOperation(value = "管理员定义重量模板明细",produces="application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",dataType = "String",name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path",dataType = "int",name = "shopId", value = "店铺Id", required = true),
            @ApiImplicitParam(paramType = "path",dataType = "int",name = "id", value = "运费模板Id", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "class", name = "vo", value = "运费模板明细", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,message = "成功"),
            @ApiResponse(code = 803,message = "运费模板中该地区已经定义")
    })
    @Audit
    @PostMapping("/shops/{shopId}/freight_models/{id}/weightItems")
    public Object postWeightItems(
            @LoginUser Long userId,
            @PathVariable("shopId") Long shopId,
            @PathVariable("id") Long id,
            @RequestBody WeightModelInfoVo vo
    ){
        return Common.decorateReturnObject(freightService.postWeightItems(vo,shopId,id));
    }
    /**
     * 管理员删除运费模板
     * @author 王薪蕾
     * @date 2020/12/9
     */

    @ApiOperation(value = "管理员定义重量模板明细",produces="application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",dataType = "String",name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path",dataType = "int",name = "shopId", value = "店铺Id", required = true),
            @ApiImplicitParam(paramType = "path",dataType = "int",name = "id", value = "运费模板Id", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "class", name = "vo", value = "运费模板明细", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,message = "成功"),
            @ApiResponse(code = 803,message = "运费模板中该地区已经定义")
    })
    @Audit
    @DeleteMapping("/shops/{shopId}/freightmodels/{id}")
    public Object deleteFreightModel(
            @LoginUser Long userId,
            @PathVariable("shopId") Long shopId,
            @PathVariable("id") Long id
    ){
        return Common.decorateReturnObject(freightService.deleteFreightModel(shopId,id));
    }
    /*
     * 管理员克隆店铺的运费模板
     * @author 史韬韬
     * @parameter shopId 店铺id
     * @parameter id 运费模板id
     * created in 2020/12/7
     * 此api的测试用例暂时没跑通，应该是数据库查询的问题，我之后再修改
     */
    @ApiOperation(value = "管理员克隆店铺的运费模板",produces="application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",dataType = "String",name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path",dataType = "Long",name = "id", value = "运费模板id", required = true),
            @ApiImplicitParam(paramType = "path",dataType = "Long",name = "shopId", value = "店铺Id", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 0,message = "成功")
    })
    @Audit
    @PostMapping("/shops/{shopId}/freightmodels/{id}/clone")
    public Object cloneFreightModel(@PathVariable Long id,@PathVariable Long shopId) {
        ReturnObject<FreightModelRetVo> retVoReturnObject = freightService.cloneFreightModel(id, shopId);
        return Common.decorateReturnObject(retVoReturnObject);
    }

    /*
     * 获得运费模板概要
     * @author 史韬韬
     * @parameter id 运费模板id
     * created in 2020/12/7
     */
    @ApiOperation(value = "获得运费模板概要",produces="application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",dataType = "String",name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path",dataType = "Long",name = "id", value = "运费模板id", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 0,message = "成功")
    })
    @Audit
    @GetMapping("/freightmodels/{id}")
    public Object getFreightModelSimpleInfo(@PathVariable Long id){
        return Common.decorateReturnObject(freightService.getFreightModelSimpleInfo(id));
    }

    /*
     * 管理员修改店铺运费模板
     * @author 史韬韬
     * @parameter id 运费模板id
     * @parameter shopId 商铺id
     * @parameter freightModelVo 修改的信息
     * created in 2020/12/7
     */
    @ApiOperation(value = "获得运费模板概要",produces="application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",dataType = "String",name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path",dataType = "Long",name = "id", value = "运费模板id", required = true),
            @ApiImplicitParam(paramType = "path",dataType = "Long",name = "shopId", value = "商铺id", required = true),
            @ApiImplicitParam(paramType = "body",dataType = "object",name = "freightModelInfoVo", value = "运费模板信息", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 0,message = "成功")
    })
    @Audit
    @PutMapping("/shops/{shopId}/freightmodels/{id}")
    public Object changeFreightModel(@PathVariable Long id, @PathVariable Long shopId, @RequestBody FreightModelInfoVo freightModelInfoVo){

        return Common.decorateReturnObject(freightService.changeFreightModel(id,shopId,freightModelInfoVo));
    }
}
