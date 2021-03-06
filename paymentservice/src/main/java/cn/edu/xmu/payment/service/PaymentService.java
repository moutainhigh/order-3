package cn.edu.xmu.payment.service;

import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.payment.dao.PaymentDao;
import cn.edu.xmu.payment.model.vo.AfterSalePaymentVo;
import cn.edu.xmu.payment.model.vo.PaymentRetVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentDao paymentDao;
    /**
     * 买家查询自己售后单的支付信息
     * @author 王薪蕾
     * @date 2020/12/9
     */
    public ReturnObject getAfterSalesPayments(Long userId, Long id) {
        ReturnObject returnObject = paymentDao.getAfterSalesPayments(userId,id);
        return returnObject;
    }
    /**
     * 管理员查询自己售后单的支付信息
     * @author 王薪蕾
     * @date 2020/12/9
     */
    public ReturnObject getAfterSalesPayments(Long userId,Long shopId, Long id) {
        ReturnObject returnObject = paymentDao.getAfterSalesPayments(userId,shopId,id);
        return returnObject;
    }

    /**
     * @author 史韬韬
     * @date 2020/12/9
     * 买家查询自己的支付信息
     */

    public ReturnObject<PaymentRetVo> getPaymentById(Long id){
        //return paymentDao.getPaymentById(id);
        return null;
    }

    /**
     * @author 史韬韬
     * @date 2020/12/10
     * 买家为售后单创建支付单
     */

    public ReturnObject<PaymentRetVo> createPaymentForAftersale(Long id, AfterSalePaymentVo afterSalePaymentVo){
        //return paymentDao.createPaymentForAftersale(id,afterSalePaymentVo);
        return null;
    }

    /**
     * @author 史韬韬
     * @date 2020/12/10
     * 管理员查看订单支付信息
     */
    public ReturnObject<PaymentRetVo> getPaymentByOrderIdAndShopId(Long id,Long shopId) {
        //return paymentDao.getPaymentByOrderIdAndShopId(id,shopId);
        return null;
    } /**
     *管理员查询订单的退款信息
     * @author 陈星如
     * @date 2020/12/9 18:13
     **/
    public ReturnObject<List> getShopsOrdersRefunds(Long shopId, Long id) {
        ReturnObject returnObject = paymentDao.getShopsOrdersRefunds(shopId, id);
        return returnObject;
    }
    /**
     *管理员查询售后订单的退款信息
     * @author 陈星如
     * @date 2020/12/9 18:10
     **/
    public ReturnObject getShopsAftersalesRefunds(Long shopId, Long id) {
        ReturnObject returnObject = paymentDao.getShopsAftersalesRefunds(shopId, id);
        return returnObject;
    }

    /**
     *管理员创建退款信息
     * @author 王薪蕾
     * @date 2020/12/11
     */
    public ReturnObject postRefunds(Long shopId, Long id,Long amount) {
        ReturnObject returnObject = paymentDao.postRefunds(shopId,id,amount);
        return returnObject;
    }

    /**
     * @author issyu 30320182200070
     * @date 2020/12/12 18:47
     * dao层实装DubboReference
     */
    /*
    public ReturnObject getPaymentStateByUserId(Long userId){
        return paymentDao.getPaymentStateByUserId(userId);
    }
    */

    /**
     * @author issyu 30320182200070
     * @date 2020/12/12 18:47
     * 不在dao层实装DubboReference
     */
    public ReturnObject getPaymentStateByOrderIds(List<Long> orderIds){
        return paymentDao.getPaymentStateByOrderIds(orderIds);
    }

    /**
     * 通过userId获取订单Id(Dubbo),通过订单id获取支付方式
     * @author issyu 30320182200070
     * @date 2020/12/14 11:31
     */
    public ReturnObject getPayPatternsByOrderId(List<Long> orderIds){
        return paymentDao.getPayPatternsByOrderId(orderIds);
    }
}
