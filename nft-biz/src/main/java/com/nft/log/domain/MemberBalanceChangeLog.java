package com.nft.log.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.nft.common.utils.IdUtils;
import com.nft.constants.Constant;
import com.nft.member.domain.Member;

import cn.hutool.core.util.NumberUtil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "member_balance_change_log")
@DynamicInsert(true)
@DynamicUpdate(true)
public class MemberBalanceChangeLog implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", length = 32)
	private String id;

	private String bizOrderNo;

	private Date changeTime;

	private String changeType;

	private Double balanceChange;

	private Double balanceBefore;

	private Double balanceAfter;

	private String note;

	@Version
	private Long version;

	@Column(name = "member_id", length = 32)
	private String memberId;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", updatable = false, insertable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private Member member;

	public static MemberBalanceChangeLog buildWithSystem(Member member, Double changeAmount) {
		MemberBalanceChangeLog log = new MemberBalanceChangeLog();
		log.setId(IdUtils.getId());
		log.setChangeTime(new Date());
		log.setChangeType(Constant.会员余额变动日志类型_系统);
		log.setBalanceChange(changeAmount);
		log.setBalanceBefore(NumberUtil.round(member.getBalance() - changeAmount, 2).doubleValue());
		log.setBalanceAfter(member.getBalance());
		log.setMemberId(member.getId());
		return log;
	}

}
