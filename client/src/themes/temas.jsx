// src/theme/temaFinature.js

const cores = {
    fundoBranco: '#FFFFFF',
    fundoEscuro: '#2F404A',
    fundoSecundario: '#F2F2F2',
    fundoLandingSecao: '#F8FAFC',
    textoPrimario: '#A0B0B9',
    textoSecundario: '#64748B',
    textoEscuro: '#1A202C',
    textoTitulo: '#2D3748',
    botaoPrimario: 'rgba(109, 122, 130, 0.5)',
    botaoHover: '#D9D9D9',
    cinzaClaro: '#C4D1D8',
    cinzaMedio: '#A0B0B9',
    bordaCard: '#E2E8F0',
    bordaCardHover: '#CBD5E0',
    corPrincipal: '#2F404A',
    corSecundaria: '#4A5568',
};

const titulo = {
    cor: '#FFFFFF',
    fontePadrao: '"Kantumruy Pro", sans-serif',
    pesoNegrito: 'bold',
    pesoSemiBold: 600,
    pesoNormal: 400,
    tamanho: '50px', // Adicionando o tamanho da fonte
};

const espacamento = {
    paddingHeroVertical: '64px',
    paddingSectionVertical: '48px',
    paddingInterno: 4,
    paddingSecao: '80px 0',
    paddingCard: '32px 24px',
    marginSecao: '64px',
    marginCard: '24px',
    marginTitulo: '16px',
    spacingGrid: 4,
    spacingGridLarge: 5,
};

const bordas = {
    raioPadrao: '16px',
    raioCard: '16px',
    raioIcone: '20px',
};

const tamanhos = {
    alturaCard: '300px',
    larguraMaxCard: '350px',
    larguraMaxSubtitulo: '900px',
    iconeSize: '72px',
    iconeFont: '32px',
    alturaGradiente: '4px',
    alturaLinha: '1px',
};

const gradientes = {
    principal: 'linear-gradient(135deg, #2F404A 0%, #4A5568 100%)',
    card: 'linear-gradient(135deg, #FFFFFF 0%, #F8FAFC 100%)',
    linha: 'linear-gradient(90deg, transparent, #E2E8F0, transparent)',
    texto: 'linear-gradient(135deg, #2F404A 0%, #4A5568 100%)',
};

const sombras = {
    icone: '0 8px 20px rgba(47, 64, 74, 0.15)',
    cardHover: '0 20px 40px rgba(47, 64, 74, 0.12), 0 8px 16px rgba(47, 64, 74, 0.08)',
    botao: '0 4px 14px rgba(196, 209, 216, 0.25)',
    botaoHover: '0 6px 20px rgba(196, 209, 216, 0.4)',
    imagem: '0 25px 50px rgba(0, 0, 0, 0.15)',
};

const transicoes = {
    padrao: 'all 0.3s cubic-bezier(0.4, 0, 0.2, 1)',
    transform: 'transform 0.3s ease',
    cor: 'color 0.3s ease',
};

export { cores, titulo, espacamento, bordas, tamanhos, gradientes, sombras, transicoes };