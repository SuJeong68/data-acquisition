package com.nhnacademy.aiotdevicegateway.node.base;

import com.nhnacademy.aiotdevicegateway.wire.Wire;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Inable노드에게서 전달받은 Message를 출력하는 Node class 입니다.
 *
 * @author 이수정
 */
@Slf4j
public abstract class OutNode extends ActiveNode implements OutAble {

    private final List<Wire> inWire;

    /**
     * inWire를 초기화합니다.
     *
     * @param id   노드 ID
     * @param name 노드 이름
     * @author 이수정
     */
    protected OutNode(String id, String name) {
        super(id, name);
        inWire = new ArrayList<>();
    }

    @Override
    public void connectInWire(Wire wire) {
        inWire.add(wire);
    }

    @Override
    public void connectInWire(int index, Wire wire) {
        if (index < 0 || getInWireLength() <= index) {
            throw new IllegalArgumentException();
        }
        log.debug("index {} wire : before {} -> after {}", index, inWire.get(index), wire);
        inWire.set(index, wire);
    }

    @Override
    public int getInWireLength() {
        return inWire.size();
    }

    public Wire getInWire(int index) {
        return inWire.get(index);
    }
}
