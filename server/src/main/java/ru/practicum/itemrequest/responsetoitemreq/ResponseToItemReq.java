package ru.practicum.itemrequest.responsetoitemreq;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.item.Item;
import ru.practicum.itemrequest.ItemRequest;

@Entity
@Table(name = "responses")
@Getter
@Setter
@ToString
public class ResponseToItemReq {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "request_id")
    private ItemRequest itemRequest;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResponseToItemReq)) return false;
        return id != null && id.equals(((ResponseToItemReq) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}