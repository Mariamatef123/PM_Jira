function validateStep1() {
    const step1 = document.getElementById('step1');
    const inputs = step1.querySelectorAll('input[required], select[required]');
    let valid = true;
    inputs.forEach(i => { if(!i.value) valid = false; });
    if(valid) {
        step1.style.display = 'none';
        document.getElementById('step2').style.display = 'block';
    } else { alert("برجاء إكمال البيانات أولاً"); }
}

function selectDay(el) {
    document.querySelectorAll('.day').forEach(d => d.classList.remove('selected'));
    el.classList.add('selected');
}

function handleSubmit(event, message) {
    event.preventDefault();
    alert(message);
    window.location.href = 'index.html';
    return false;
}