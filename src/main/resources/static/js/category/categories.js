// 카테고리 아이콘 (key: 아이콘 식별자, value: { svg: 마크업, label: 툴팁/의미 })
// width/height는 CSS(.cat-icon svg / .icon-picker button svg)에서 컨텍스트별로 지정
// TODO: 추후 아이콘을 DB에서 관리하게 되면 이 객체 대신 서버 응답(아이콘 목록 API)으로 대체
const CATEGORY_ICONS = {
  food: { svg: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M3 2v7c0 1.1.9 2 2 2s2-.9 2-2V2M5 2v20M21 15V2a5 5 0 0 0-3 4.5V11c0 1 .5 2 1.5 2.5L21 15v6"/></svg>', label: '식비' },
  transport: { svg: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="7" cy="18" r="2"/><circle cx="17" cy="18" r="2"/><path d="M2 12h19.6"/></svg>', label: '교통' },
  housing: { svg: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/><path d="M9 22V12h6v10"/></svg>', label: '주거' },
  culture: { svg: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M9 18V5l12-2v13"/><circle cx="6" cy="18" r="3"/><circle cx="18" cy="16" r="3"/></svg>', label: '문화/여가' },
  shopping: { svg: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M6 2 3 6v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2V6l-3-4z"/><path d="M3 6h18M16 10a4 4 0 0 1-8 0"/></svg>', label: '쇼핑' },
  subscription: { svg: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"/><path d="M12 6v6l4 2"/></svg>', label: '구독' },
  communication: { svg: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6 19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 4.11 2h3a2 2 0 0 1 2 1.72c.127.96.361 1.902.7 2.81a2 2 0 0 1-.45 2.11L8.09 9.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45c.908.339 1.85.573 2.81.7A2 2 0 0 1 22 16.92z"/></svg>', label: '통신' },
  education: { svg: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M22 10 12 5 2 10l10 5 10-5Z"/><path d="M6 12.5V17c0 1.5 3 3 6 3s6-1.5 6-3v-4.5"/></svg>', label: '교육' },
  medical: { svg: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M19 14c1.49-1.46 3-3.21 3-5.5A5.5 5.5 0 0 0 16.5 3c-1.76 0-3 .5-4.5 2-1.5-1.5-2.74-2-4.5-2A5.5 5.5 0 0 0 2 8.5c0 2.29 1.51 4.04 3 5.5l7 7Z"/><path d="M3.22 8.5h3l1.5-3 3 6 1.5-3h5.56"/></svg>', label: '의료' },
  allowance: { svg: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M19 5c-1.5 0-2.8 1.4-3 2-3.5-1.5-11-.3-11 5 0 1.8 0 3 2 4.5V20h4v-2h3v2h4v-4c1-.5 1.7-1 2-2h2v-4h-2c0-1-.5-1.7-1-2h0V5z"/><circle cx="8" cy="10" r="1"/></svg>', label: '용돈' },
  salary: { svg: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="2" y="6" width="20" height="12" rx="2"/><circle cx="12" cy="12" r="2"/></svg>', label: '급여' },
  side: { svg: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M12 1v22M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6"/></svg>', label: '부수입' },
  interest: { svg: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M23 6l-9.5 9.5-5-5L1 18"/><path d="M17 6h6v6"/></svg>', label: '이자/투자' },
  etc: { svg: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M12 2a3 3 0 0 0-3 3v1H5a3 3 0 0 0 0 6h4v4a3 3 0 0 0 6 0v-4h4a3 3 0 0 0 0-6h-4V5a3 3 0 0 0-3-3z"/></svg>', label: '기타' },
};

// data-icon 속성이 달린 엘리먼트에 실제 SVG와 title(툴팁)을 채워넣음 (카드 아이콘 + 아이콘 피커 버튼)
function renderCategoryIcons() {
  document.querySelectorAll('[data-icon]').forEach(el => {
    const icon = CATEGORY_ICONS[el.dataset.icon];
    if (!icon) return;
    el.innerHTML = icon.svg;
    el.title = icon.label;
  });
}

// 마지막으로 조회한 카테고리 목록 (편집 모달에 채울 원본 데이터 조회용)
let allCategories = [];
// 편집 중인 카테고리 id (null이면 추가 모드)
let editingCategoryId = null;

// 이벤트 리스너
document.addEventListener('DOMContentLoaded', () => {
  loadCategories();
  renderCategoryIcons();

  document.querySelectorAll('.cat-tab').forEach(tab => {
    tab.addEventListener('click', () => switchTab(tab.dataset.type));
  });

  // 페이지 헤더의 추가 버튼 + 빈 상태에 노출되는 추가 버튼 모두 동일하게 동작
  document.querySelectorAll('#btnOpenModal, .js-open-modal').forEach(btn => {
    btn.addEventListener('click', openCreateModal);
  });

  // 편집/삭제 버튼은 카드가 fetch 응답으로 매번 새로 그려지므로 그리드에 위임
  document.getElementById('grid-expense').addEventListener('click', handleCardAction);
  document.getElementById('grid-income').addEventListener('click', handleCardAction);

  document.getElementById('categoryModal').addEventListener('click', e => {
    if (e.target === e.currentTarget) closeModal();
  });

  document.querySelector('.modal-close').addEventListener('click', closeModal);
  document.querySelector('.btn-cancel').addEventListener('click', closeModal);

  document.querySelectorAll('#typeToggle button').forEach(btn => {
    btn.addEventListener('click', () => selectType(btn));
  });

  document.querySelectorAll('#iconPicker button').forEach(btn => {
    btn.addEventListener('click', () => selectIcon(btn));
  });

  document.getElementById('categoryName').addEventListener('input', () => {
    document.getElementById('nameError').classList.add('d-none'); // 비어있는 값일 때
  });

  // 추가하기 버튼 클릭 시
  document.getElementById('submitCategoryBtn').addEventListener('click', submitCategory);
});

// 탭 전환
function switchTab(type) {
  document.querySelectorAll('.cat-tab').forEach(t => t.classList.remove('active'));
  document.querySelector(`.cat-tab[data-type="${type}"]`).classList.add('active'); // 탭 전환 데이터 타입 설정
  document.getElementById('panel-expense').classList.toggle('d-none', type !== 'expense');
  document.getElementById('panel-income').classList.toggle('d-none', type !== 'income');
}

// 카테고리 목록 조회 (탭 전환처럼 페이지 전환이 필요 없으므로 fetch로 처리)
async function loadCategories() {
  try {
    const res = await fetch('/api/categories');
    if (!res.ok) throw new Error('카테고리 조회에 실패했습니다.');
    renderCategories(await res.json());
  } catch (e) {
    console.error(e);
    renderCategories([]);
  }
}

// 조회한 카테고리를 지출/수입으로 나눠 각 패널에 렌더링
function renderCategories(categories) {
  allCategories = categories;

  const expense = categories.filter(c => c.categoryType === 'EXPENSE');
  const income = categories.filter(c => c.categoryType === 'INCOME');

  renderPanel('expense', expense);
  renderPanel('income', income);

  document.querySelector('.cat-tab[data-type="expense"] .cat-tab-count').textContent = expense.length;
  document.querySelector('.cat-tab[data-type="income"] .cat-tab-count').textContent = income.length;
}

// 카테고리가 없으면 빈 상태를, 있으면 그리드를 노출
function renderPanel(type, categories) {
  const grid = document.getElementById(`grid-${type}`);
  const empty = document.getElementById(`empty-${type}`);

  grid.innerHTML = '';

  if (categories.length === 0) {
    grid.classList.add('d-none');
    empty.classList.remove('d-none');
    return;
  }

  empty.classList.add('d-none');
  grid.classList.remove('d-none');
  grid.innerHTML = categories.map(categoryCardHtml).join('');
  renderCategoryIcons();
}

// 카테고리 카드 마크업 (categoryName/iconKey는 사용자 입력값이므로 반드시 이스케이프)
function categoryCardHtml(cat) {
  const typeClass = cat.categoryType === 'EXPENSE' ? 'expense' : 'income';
  const registeredAt = cat.insertDate ? cat.insertDate.slice(0, 10).replaceAll('-', '.') : '';

  return `
    <div class="cat-card" data-id="${cat.categoryId}">
      <span class="cat-icon ${typeClass}" data-icon="${escapeHtml(cat.iconKey)}"></span>
      <div class="flex-fill">
        <div class="cat-name">${escapeHtml(cat.categoryName)}</div>
        <div class="cat-count">${registeredAt} 등록</div>
      </div>
      <div class="d-flex align-items-center gap-1">
        <button type="button" class="cat-action edit" title="편집">
          <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor"
               stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/>
            <path d="M18.5 2.5a2.12 2.12 0 0 1 3 3L12 15l-4 1 1-4Z"/>
          </svg>
        </button>
        <button type="button" class="cat-action delete" title="삭제">
          <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor"
               stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M3 6h18M8 6V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2m3 0v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6"/>
          </svg>
        </button>
      </div>
    </div>
  `;
}

function escapeHtml(str) {
  return String(str)
    .replaceAll('&', '&amp;')
    .replaceAll('<', '&lt;')
    .replaceAll('>', '&gt;')
    .replaceAll('"', '&quot;')
    .replaceAll("'", '&#39;');
}

// 카드에 위임된 편집/삭제 버튼 클릭 처리
function handleCardAction(e) {
  const card = e.target.closest('.cat-card');
  if (!card) return;

  const categoryId = Number(card.dataset.id);

  if (e.target.closest('.cat-action.edit')) {
    const category = allCategories.find(c => c.categoryId === categoryId);
    if (category) openEditModal(category);
  } else if (e.target.closest('.cat-action.delete')) {
    deleteCategory(categoryId);
  }
}

// 추가 모달 열기 (현재 보고 있는 탭의 유형을 기본 선택값으로 사용)
function openCreateModal() {
  const activeType = document.querySelector('.cat-tab.active').dataset.type;
  resetModal(activeType);
  document.getElementById('categoryModal').classList.remove('d-none');
}

// 편집 모달 열기 (기존 카테고리 값으로 폼을 채움)
function openEditModal(category) {
  resetModal();
  editingCategoryId = category.categoryId;

  document.querySelector('#categoryModal h2').textContent = '카테고리 편집';
  document.getElementById('categoryName').value = category.categoryName;

  const type = category.categoryType.toLowerCase();
  document.querySelectorAll('#typeToggle button').forEach(b => {
    b.classList.toggle('active', b.dataset.type === type);
  });
  document.getElementById('iconPicker').dataset.type = type;

  document.querySelectorAll('#iconPicker button').forEach(b => {
    b.classList.toggle('active', b.dataset.icon === category.iconKey);
  });

  document.getElementById('submitCategoryBtn').textContent = '저장하기';
  document.getElementById('categoryModal').classList.remove('d-none');
}

// 모달 닫기
function closeModal() {
  document.getElementById('categoryModal').classList.add('d-none');
}

// 모달 초기화
function resetModal(type = 'expense') {
  editingCategoryId = null;
  document.querySelector('#categoryModal h2').textContent = '카테고리 추가';
  document.getElementById('categoryName').value = '';
  document.getElementById('nameError').classList.add('d-none');
  document.getElementById('submitError').classList.add('d-none');

  document.querySelectorAll('#typeToggle button').forEach(b => {
    b.classList.toggle('active', b.dataset.type === type);
  });
  document.getElementById('iconPicker').dataset.type = type;

  document.querySelectorAll('#iconPicker button').forEach(b => b.classList.remove('active'));
  document.querySelector('#iconPicker button').classList.add('active');

  const submitBtn = document.getElementById('submitCategoryBtn');
  submitBtn.disabled = false;
  submitBtn.textContent = '추가하기';
}

// 유형 선택 (아이콘 피커의 활성 색상도 유형에 맞춰 전환)
function selectType(btn) {
  document.querySelectorAll('#typeToggle button').forEach(b => b.classList.remove('active'));
  btn.classList.add('active');
  document.getElementById('iconPicker').dataset.type = btn.dataset.type;
}

// 아이콘 선택
function selectIcon(btn) {
  document.querySelectorAll('#iconPicker button').forEach(b => b.classList.remove('active'));
  btn.classList.add('active');
}

// 카테고리 추가/편집 (editingCategoryId 유무로 분기)
async function submitCategory() {
  const nameInput = document.getElementById('categoryName');
  const name = nameInput.value.trim();

  if (!name) {
    document.getElementById('nameError').classList.remove('d-none');
    nameInput.focus();
    return;
  }

  const type = document.querySelector('#typeToggle button.active').dataset.type;
  const icon = document.querySelector('#iconPicker button.active').dataset.icon;
  const isEdit = editingCategoryId !== null;

  const submitBtn = document.getElementById('submitCategoryBtn');
  submitBtn.disabled = true;
  submitBtn.textContent = '저장 중...';

  try {
    const res = await fetch(isEdit ? `/api/categories/${editingCategoryId}` : '/api/categories', {
      method: isEdit ? 'PUT' : 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        categoryType: type.toUpperCase(), // CategoryType enum(EXPENSE/INCOME)과 매칭
        categoryName: name,
        iconKey: icon,
      }),
    });

    if (res.ok) {
      closeModal();
      await loadCategories();
    } else {
      const data = await res.json().catch(() => ({}));
      showSubmitError(data.message || '저장에 실패했습니다. 다시 시도해주세요.');
      submitBtn.disabled = false;
      submitBtn.textContent = isEdit ? '저장하기' : '추가하기';
    }
  } catch {
    showSubmitError('네트워크 오류가 발생했습니다. 다시 시도해주세요.');
    submitBtn.disabled = false;
    submitBtn.textContent = isEdit ? '저장하기' : '추가하기';
  }
}

// 카테고리 삭제 (soft delete: DELETE 요청 -> is_deleted = true 처리)
async function deleteCategory(categoryId) {
  if (!confirm('이 카테고리를 삭제하시겠습니까?')) return;

  try {
    const res = await fetch(`/api/categories/${categoryId}`, { method: 'DELETE' });
    if (res.ok) {
      await loadCategories();
    } else {
      alert('삭제에 실패했습니다. 다시 시도해주세요.');
    }
  } catch {
    alert('네트워크 오류가 발생했습니다. 다시 시도해주세요.');
  }
}

function showSubmitError(msg) {
  const el = document.getElementById('submitError');
  el.textContent = msg;
  el.classList.remove('d-none');
}